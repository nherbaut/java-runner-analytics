package fr.pantheonsorbonne.ufr27.miashs.poo.business;

import fr.pantheonsorbonne.ufr27.miashs.poo.client.ProjectFactoryResource;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.AssignmentForm;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.FileType;
import fr.pantheonsorbonne.ufr27.miashs.poo.model.InjectedFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class SkeletonDownloadService {

    @RestClient
    ProjectFactoryResource projectFactoryResource;

    @Inject
    ClassGenerationService classGenerationService;

    public InputStream getProjectSkeleton(AssignmentForm data) throws IOException {
        String codeItem = classGenerationService.getItemJAVA(data);
        String pageConstant = classGenerationService.getContentProxy(data.assignementURL());
        String cachedPage = classGenerationService.generateCachedPage(data.assignementURL());

        String itemScrapper = classGenerationService.getItemsScrapperJAVA(data);
        String ItemAnalyzer = classGenerationService.getItemsAnalyzerJAVA(data);
        AtomicInteger scrappedCount = new AtomicInteger();
        Response resp = projectFactoryResource.post("aHR0cHM6Ly9naXRodWIuY29tL1VGUjI3LzIwMjMtTDItUE9PLXRlbXBsYXRlLmdpdA==",
                Stream.concat(
                        Arrays.stream((new String[]{codeItem, pageConstant, itemScrapper, ItemAnalyzer}))
                                .map(s -> new InjectedFile(FileType.SOURCE, s, "")),
                        Arrays.stream((new String[]{cachedPage}))
                                .map(s -> new InjectedFile(FileType.ASSET, s, "scrapped" + (scrappedCount.getAndIncrement()) + ".txt"))).collect(Collectors.toList()));

        var is = resp.readEntity(InputStream.class);
        return is;
    }
}
