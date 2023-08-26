package fr.pantheonsorbonne.ufr27.miage.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pantheonsorbonne.ufr27.miage.model.PayloadModel;
import fr.pantheonsorbonne.ufr27.miage.model.Result;
import fr.pantheonsorbonne.ufr27.miage.service.BuilderAndCompiler;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.concurrent.TimeUnit;
import io.quarkus.logging.Log;
@QuarkusMain
public class JavaCodeRunnerMain implements QuarkusApplication {

    @Inject
    BuilderAndCompiler builderAndCompiler;


    @Override
    public int run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BufferedInputStream bis = new BufferedInputStream(System.in);
        BufferedOutputStream bos = new BufferedOutputStream(System.out);
        PayloadModel payloadModel = mapper.readValue(bis, PayloadModel.class);
        Log.info(payloadModel.toString());
        Result result = builderAndCompiler.buildAndCompile(payloadModel,10, TimeUnit.SECONDS);
        Log.info(result.toString());
        mapper.writeValue(bos, result);
        return 0;
    }
}