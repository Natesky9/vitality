package com.natesky9;

import net.runelite.api.Client;
import net.runelite.api.RuneLiteObject;

import javax.inject.Inject;

public class ClientSecretDisable implements Runnable {
    @Inject SecretFeature secret;
    //final SecretFeature secret;
    public ClientSecretDisable(SecretFeature secret)
    {
        this.secret = secret;
    }
    @Override
    public void run() {
        for (RuneLiteObject fool: secret.fools)
        {
            fool.setActive(false);
        }
        secret.fools.clear();
    }
}
