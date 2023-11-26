package fr.pantheonsorbonne.ufr27.miashs.poo.business;

import com.squareup.javapoet.*;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.FormData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import wtf.metio.javapoet.TypeGuesser;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

@ApplicationScoped
public class ClassGenerationService {

    public static final String FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO = "fr.pantheonsorbonne.ufr27.miashs.poo";

    public static final String PAGE_SOURCE = "pageSource";
    public static final String CONTENT_PROXY = "ContentProxy";
    public static final String PAGE_SOURCE_CONSTANT = "PAGE_SOURCE";
    public static final String REMOTE_URL = "REMOTE_URL";
    public static final String ITEM = "Item";


    public ClassGenerationService(WebPageSourceCodeService webPageSourceCodeService) {
        this.webPageSourceCodeService = webPageSourceCodeService;
    }

    @Inject
    WebPageSourceCodeService webPageSourceCodeService;

    public String getContentProxy(String url) throws IOException {
        FieldSpec constant1 = FieldSpec.builder(String.class, "CACHED_FILE_PATH", Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC).initializer("$S", "src/main/resources/scrapped0.txt").build();
        FieldSpec constant2 = FieldSpec.builder(String.class, REMOTE_URL, Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC).initializer("$S", url.trim()).build();

        ClassName webPageFetcher = ClassName.get(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, "WebPageFetcher");
        MethodSpec getCached = MethodSpec.methodBuilder("getCached")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(IOException.class)
                .beginControlFlow("try($T reader = new $T(new $T(new $T($L),$S)))", BufferedReader.class, BufferedReader.class, InputStreamReader.class, FileInputStream.class, constant1.name,"UTF-8")
                .addStatement("return reader.lines().collect($T.joining($S))", Collectors.class, "\n")
                .nextControlFlow("catch($T ie)", IOException.class)
                .addStatement("throw new $T(ie)", RuntimeException.class)
                .endControlFlow()
                .returns(String.class).build();
        MethodSpec getFresh = MethodSpec.methodBuilder("getFresh")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addException(IOException.class)
                .addStatement("return $T.fetchResource($L)",webPageFetcher,REMOTE_URL)
                .build();

        TypeSpec.Builder itemScrapperBuilder = TypeSpec.classBuilder(CONTENT_PROXY)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(constant1)
                .addField(constant2)
                .addMethod(getCached)
                .addMethod(getFresh);

        JavaFile javaFile = JavaFile.builder(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, itemScrapperBuilder.build())
                .build();
        StringWriter sg = new StringWriter();
        javaFile.writeTo(sg);
        return sg.toString();
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static void addGetterAndSetter(FieldSpec fieldSpec, TypeSpec.Builder classBuilder) {
        addGetter(fieldSpec, classBuilder);
        addSetter(fieldSpec, classBuilder);
    }


    private static void addSetter(FieldSpec fieldSpec, TypeSpec.Builder classBuilder) {
        String setterName = "set" + capitalizeFirstLetter(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(setterName).addModifiers(Modifier.PUBLIC);
        methodBuilder.addParameter(fieldSpec.type, fieldSpec.name);
        methodBuilder.addStatement("this." + fieldSpec.name + "=" + fieldSpec.name);
        classBuilder.addMethod(methodBuilder.build());
    }


    public static void addGetter(FieldSpec fieldSpec, TypeSpec.Builder classBuilder) {
        String getterName = "get" + capitalizeFirstLetter(fieldSpec.name);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getterName).returns(fieldSpec.type).addModifiers(Modifier.PUBLIC);
        methodBuilder.addStatement("return this." + fieldSpec.name);
        classBuilder.addMethod(methodBuilder.build());
    }


    public String getItemsScrapperJAVA(AssignmentForm assignmentForm) throws IOException {

        ClassName itemClass = ClassName.get("fr.pantheonsorbonne.ufr27.miashs.poo", "Item");

        TypeSpec.Builder itemScrapperBuilder = TypeSpec.classBuilder("ItemsScrapper")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        MethodSpec.Builder parseSourceBuilder = MethodSpec.methodBuilder("parseSource").addParameter(String.class, PAGE_SOURCE)
                .addStatement("ArrayList<$T> itemList = new ArrayList<>()", itemClass)
                .beginControlFlow("while (true)")
                .addComment("add code here")
                .addStatement("$T item = new $T()", itemClass, itemClass);

        assignmentForm.formData().stream().filter(fd -> fd.scope().equals("item")).forEach(fd -> {
            parseSourceBuilder.addStatement("item.$L(null)", "set" + capitalizeFirstLetter(fd.name()));
        });
        parseSourceBuilder.addStatement("itemList.add(item)")
                .beginControlFlow("if(true)")
                .addComment("on a fini d'extraire les item")
                .addStatement("break")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return itemList")
                .returns(ParameterizedTypeName.get(ClassName.get(ArrayList.class), itemClass).withoutAnnotations());
        itemScrapperBuilder.addMethod(parseSourceBuilder.build());

        JavaFile javaFile = JavaFile.builder(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, itemScrapperBuilder.build())
                .build();

        StringWriter sg = new StringWriter();
        javaFile.writeTo(sg);
        return sg.toString();

    }

    public String getItemJAVA(AssignmentForm assignmentForm) throws IOException {


        TypeSpec.Builder itemScrapperBuilder = TypeSpec.classBuilder(ITEM)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        assignmentForm.formData().stream().filter(fd -> fd.scope().equals("item")).forEach(fd -> {
                    try {
                        FieldSpec fieldSpec = FieldSpec.builder(getType(fd), fd.name(), Modifier.PRIVATE).build();
                        itemScrapperBuilder.addField(fieldSpec);
                        addGetterAndSetter(FieldSpec.builder(getType(fd), fd.name(), Modifier.PUBLIC).build(), itemScrapperBuilder);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );


        JavaFile javaFile = JavaFile.builder(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, itemScrapperBuilder.build())
                .build();

        StringWriter sg = new StringWriter();
        javaFile.writeTo(sg);
        return sg.toString();
    }

    protected TypeName getType(FormData fd) throws ClassNotFoundException {
        return TypeGuesser.guessTypeName(fd.type());
    }


    public String getItemsAnalyzerJAVA(AssignmentForm data) throws IOException {
        var typeSpec = TypeSpec.classBuilder("ItemAnalyzer").addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        var className = ClassName.get(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, "Item");

        typeSpec.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(ArrayList.class), className), "items").addModifiers(Modifier.PRIVATE).initializer(CodeBlock.of("new $T<>()", ClassName.get(ArrayList.class))).build());
        typeSpec.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(
                        ParameterizedTypeName.get(ClassName.get(ArrayList.class), className), "items")
                .addStatement("this.items=items").build());

        data.formData().stream().filter(fd -> fd.scope().equals("items")).forEach(fd -> {
                    try {
                        typeSpec.addMethod(
                                MethodSpec.methodBuilder(
                                                "get" + capitalizeFirstLetter(fd.name()))
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(getType(fd))
                                        .addComment("code here")
                                        .addStatement("return null").build());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        JavaFile javaFile = JavaFile.builder(FR_PANTHEONSORBONNE_UFR_27_MIASHS_POO, typeSpec.build())
                .build();

        StringWriter sg = new StringWriter();
        javaFile.writeTo(sg);
        return sg.toString();


    }

    public String generateCachedPage(String url) {
        return webPageSourceCodeService.getURLContent(url);
    }
}
