package com.example.grooveboard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp desencadena la generación de código de Hilt,
 * creando un contenedor de dependencias a nivel de aplicación (SingletonComponent).
 */
@HiltAndroidApp
class GrooveBoardApp : Application()