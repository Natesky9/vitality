package com.natesky9;

import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.ModelData;

import javax.inject.Inject;

public class SecretFeature {

    @Inject
    private Client client;
    @Inject
    private VitalityPlugin plugin;

    private final ModelData[] data;
    private ModelData modelData;
    private final Model model;

    public SecretFeature(Client client)
    {

        data = new ModelData[]
                {
                        client.loadModelData(31794),
                        client.loadModelData(214),
                        client.loadModelData(250),
                        client.loadModelData(31805),
                        client.loadModelData(31797),
                        client.loadModelData(177),
                        client.loadModelData(31783),
                        client.loadModelData(181),
                        client.loadModelData(31911),
                        client.loadModelData(31889),
                };
        modelData = client.mergeModels(data);
        model = modelData.light();
    }



    public Model getBrassica()
    {
        return model;
    }
}
