package co.mil.imi.models;

import co.mil.imi.models.flowable.HydraUpload;
import co.mil.imi.models.resources.ClientResource;
import co.mil.imi.models.resources.EntitiesResource;
import co.mil.imi.models.resources.FileResource;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
//import io.dropwizard.assets.AssetsBundle;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class ImageLoaderApplication extends Application<ImageLoaderConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ImageLoaderApplication().run(args);
    }

    @Override
    public String getName() {
        return "image-loader";
    }

    @Override
    public void initialize(final Bootstrap<ImageLoaderConfiguration> bootstrap) {
//        bootstrap.addBundle(new AssetsBundle("/assets", "/images"));
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/images"));
        bootstrap.addBundle(new ViewBundle<>());
    }

    @Override
    public void run(final ImageLoaderConfiguration configuration,
                    final Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(FileResource.class);
        environment.jersey().register(ClientResource.class);
        environment.jersey().register(EntitiesResource.class);
//        HydraUpload.go();
    }
}
