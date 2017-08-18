package kenticocloud.kenticoclouddancinggoat.kentico_cloud.models;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.common.IField;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.item.IContentItem;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.interfaces.item.item.IContentItemSystemAttributes;

/**
 * Created by RichardS on 17. 8. 2017.
 */

public abstract class ContentItem implements IContentItem{

    private IContentItemSystemAttributes _system;
    private List<IField> _elements;

    @Override
    public IContentItemSystemAttributes getSystem() {
        return _system;
    }

    @Override
    public void setContentItemSystemAttributes(@NonNull IContentItemSystemAttributes system) {
        _system = system;
    }

    @Override
    public List<IField> getElements() {
        return _elements;
    }

    @Override
    public void setElements(@NonNull List<IField> elements) {
        _elements = elements;
    }

    @Override
    public IField getField(@NonNull String fieldName) {
        for (IField field: _elements) {
            if (field.getCodename().equalsIgnoreCase(fieldName)){
                return field;
            }
        }

        // no field found
        return null;
    }

    @Override
    public String GetStringValue(@NonNull String fieldName) {
        IField field = getField(fieldName);
        return field == null ? null : (String)field.getValue();
    }

    @Override
    public int GetIntValue(@NonNull String fieldName) throws NullPointerException{
        IField field = getField(fieldName);
        return field == null ? null : (int)field.getValue();
    }

    @Override
    public Date getDateValue(@NonNull String fieldName) {
        IField field = getField(fieldName);
        return field == null ? null : (Date)field.getValue();
    }
}
