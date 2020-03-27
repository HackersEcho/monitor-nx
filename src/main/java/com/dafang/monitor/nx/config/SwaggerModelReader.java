package com.dafang.monitor.nx.config;


import cn.hutool.core.util.IdUtil;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:读取自定义忽略的dto属性并动态生成model
 * @author: echo
 * @createDate: 2020/3/14
 * @version: 1.0
 */
@Configuration
@Order   //plugin加载顺序，默认是最后加载
public class SwaggerModelReader implements ParameterBuilderPlugin {
    @Autowired
    private TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<ApiIgp> apiIgp = methodParameter.findAnnotation(ApiIgp.class);
        Optional<Apicp> apicp = methodParameter.findAnnotation(Apicp.class);
        if (apiIgp.isPresent() || apicp.isPresent()) {
            Class originClass = parameterContext.resolvedMethodParameter().getParameterType().getErasedType();
            String name = originClass.getSimpleName() + "Model" + IdUtil.objectId();  //model 名称
            String properties = null;
            Integer annoType = 0;
            if (apiIgp.isPresent()){
                properties = apiIgp.get().value();
            }else {
                properties = apicp.get().value();
                annoType = 1;
            }
            try {
                parameterContext.getDocumentationContext()
                        .getAdditionalModels()
                        .add(typeResolver.resolve(createRefModelIgp(properties, name, originClass,annoType)));  //像documentContext的Models中添加我们新生成的Class
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            parameterContext.parameterBuilder()  //修改model参数的ModelRef为我们动态生成的class
                    .parameterType("body")
                    .modelRef(new ModelRef(name))
                    .name(name);
        }
    }


    /**
     * 根据propertys中的值动态生成含有Swagger注解的javaBeen
     */
    private Class createRefModelIgp(String propertys, String name, Class origin, Integer annoType) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(origin.getPackage().getName()+"."+name);
        try {
            Field[] fields = origin.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            List<String> dealProperties = Arrays.asList(propertys.replace(" ","").split(","));//去掉空格并用逗号分割
            List<Field> dealFileds = fieldList.stream().filter(
                    s -> annoType == 0 ?  (!(dealProperties.contains(s.getName()))) : dealProperties.contains(s.getName())
            ).collect(Collectors.toList());
            createCtFileds(dealFileds,ctClass);
            return ctClass.toClass();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public void createCtFileds(List<Field> dealFileds, CtClass ctClass) throws CannotCompileException, NotFoundException {
        for (Field field : dealFileds) {
            CtField ctField = new CtField(ClassPool.getDefault().get(field.getType().getName()), field.getName(), ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String apiModelPropertyValue = java.util.Optional.ofNullable(annotation).map(s -> s.value()).orElse("");
            if (StringUtils.isNotBlank(apiModelPropertyValue)) { //添加model属性说明
                ConstPool constPool = ctClass.getClassFile().getConstPool();
                AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
                ann.addMemberValue("value", new StringMemberValue(apiModelPropertyValue, constPool));
                attr.addAnnotation(ann);
                ctField.getFieldInfo().addAttribute(attr);
            }
            ctClass.addField(ctField);
        }
    }
}