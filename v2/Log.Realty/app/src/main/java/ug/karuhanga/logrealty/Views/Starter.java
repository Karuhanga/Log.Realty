package ug.karuhanga.logrealty.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ug.karuhanga.logrealty.Controllers.Controller;
import ug.karuhanga.logrealty.R;

public class Starter extends AppCompatActivity implements Controller.StarterControllerExternalInterface {
    private StarterActivityExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.controller= Controller.injectStarterActivityctivityExternalInterface(this);
        setContentView(R.layout.starter_activity);
        performInitActions();
    }

    /**
     * UI Changers
     */

    private void showQuickGuide() {
    }

    /**
     * Major event starters
     */

    private void performInitActions() {
        controller.startInits();
    }

    /**
     * External Interaction methods and interfaces
     */

    public interface StarterActivityExternalInterface{

        void startInits();
    }

    @Override
    public Context requestContext() {
        return this;
    }

    @Override
    public void startFirstLaunchActions() {
        showQuickGuide();
    }

    /**
     * Post the fact actions
     */

    @Override
    public void onActionsCompleted(){
        startGistActivity();
        finish();
    }

    private void startGistActivity() {
        startActivity(new Intent(Starter.this, Gist.class));
    }
}
