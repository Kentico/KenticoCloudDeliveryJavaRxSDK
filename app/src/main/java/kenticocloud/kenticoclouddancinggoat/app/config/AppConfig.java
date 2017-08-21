package kenticocloud.kenticoclouddancinggoat.app.config;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import kenticocloud.kenticoclouddancinggoat.data.models.Cafe;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.item.IContentItem;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.item.TypeResolver;

/**
 * Created by RichardS on 18. 8. 2017.
 */

public class AppConfig {
    public final static String KenticoCloudProjectId = "683771be-aa26-4887-b1b6-482f56418ffd";

    public final static List<TypeResolver> getTypeResolvers(){
        // Type resolvers are responsible for creating the strongly typed object out of type
        List<TypeResolver> typeResolvers = new ArrayList<>();
        typeResolvers.add(new TypeResolver("cafe", new Function<Void, IContentItem>() {
            @Nullable
            @Override
            public IContentItem apply(@Nullable Void input) {
                return new Cafe();
            }
        }));

        return typeResolvers;
    }

}
