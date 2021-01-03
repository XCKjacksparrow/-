/**
 * This file is used specifically and only for development. It installs
 * `electron-debug` & `vue-devtools`. There shouldn't be any need to
 *  modify this file, but it can be used to extend your development
 *  environment.
 */
import { BrowserWindow } from 'electron'
/* eslint-disable */
// Set environment for development
process.env.NODE_ENV = 'development'

// Install `electron-debug` with `devtron`
require('electron-debug')({ showDevTools: false })

// Install `vue-devtools`
require('electron').app.on('ready', () => {
  let installExtension = require('electron-devtools-installer')
  installExtension.default(installExtension.VUEJS_DEVTOOLS)
    .then(() => {})
    .catch(err => {
      console.log('Unable to install `vue-devtools`: \n', err)
    })
  // BrowserWindow.addDevToolsExtension("C:\\Users\\A\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\vue-devtools-dev\\vue-devtools-dev\\shells\\chrome");
})

// Require `main` process to boot app
require('./index')
