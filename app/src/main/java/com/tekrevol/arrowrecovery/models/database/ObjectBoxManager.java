package com.tekrevol.arrowrecovery.models.database;
import com.tekrevol.arrowrecovery.BaseApplication;
import com.tekrevol.arrowrecovery.models.receiving_model.MaterialHistoryModel;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class ObjectBoxManager {
    public static ObjectBoxManager INSTANCE = null;
    private BaseApplication app;

    public BoxStore getBoxStore() {
        return app.getBoxStore();
    }

    public Box<MaterialHistoryModel> getMaterialHistoryModelDBBox() {
        return getBoxStore().boxFor(MaterialHistoryModel.class);
    }

    public long putMaterialDBModel( ArrayList<MaterialHistoryModel> arrayList) {

        getMaterialHistoryModelDBBox().put(arrayList);

        MaterialHistoryModel materialDBModel = new MaterialHistoryModel();

        return getMaterialHistoryModelDBBox().put(materialDBModel);

    }


/*
    public ArrayList<MaterialHistoryModel> getAllHistoryModels() {
        List<MaterialHistoryModel> all = getMaterialHistoryModelDBBox().getAll();

        ArrayList<MaterialHistoryModel> mediaModels = new ArrayList<>();

        for (MaterialHistoryModel generalDBModel : all) {
            mediaModels.add(generalDBModel);
        }

        return mediaModels;
    }*/


    public void removeAllDB() {
        getMaterialHistoryModelDBBox().removeAll();
    }

    private ObjectBoxManager(BaseApplication app) {
        this.app = app;
    }


    public static ObjectBoxManager getInstance(BaseApplication app) {
        if (INSTANCE == null)
            INSTANCE = new ObjectBoxManager(app);

        return INSTANCE;
    }

}
