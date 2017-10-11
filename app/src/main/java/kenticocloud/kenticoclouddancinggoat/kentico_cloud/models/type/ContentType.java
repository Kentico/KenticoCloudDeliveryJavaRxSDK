package kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.type;

import java.util.List;

import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.element.ContentTypeElement;

public class ContentType {

    private ContentTypeSystemAttributes system;
    private List<ContentTypeElement> elements;

    public ContentType(
            ContentTypeSystemAttributes system,
            List<ContentTypeElement> elements
    ){
        this.system = system;
        this.elements = elements;
    }


    public ContentTypeSystemAttributes getSystem() {
        return this.system;
    }

    public List<ContentTypeElement> getElements() {
        return this.elements;
    }
}
