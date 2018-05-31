package com.inka.netsync.di.component;

import com.inka.netsync.di.PerService;
import com.inka.netsync.di.module.ServiceModule;

import dagger.Component;

/**
 * Created by birdgang on 2017. 2. 26..
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
}