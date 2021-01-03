import { app, BrowserWindow } from 'electron'
const ipcMain = require('electron').ipcMain

/**
 * Set `__static` path to static files in production
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-static-assets.html
 */
if (process.env.NODE_ENV !== 'development') {
  global.__static = require('path').join(__dirname, '/static').replace(/\\/g, '\\\\')
}

let mainWindow
let downloadpath = ''
let folderpath = ''
const winURL = process.env.NODE_ENV === 'development'
  ? `http://localhost:9080`
  : `file://${__dirname}/index.html`

function createWindow() {
  /**
   * Initial window options
   */
  mainWindow = new BrowserWindow({
    height: 1080,
    useContentSize: true,
    width: 1920,
    webPreferences: { webSecurity: false, nodeIntegration: true }
  })

  mainWindow.loadURL(winURL)
  console.log(winURL)

  mainWindow.on('closed', () => {
    mainWindow = null
  })
  /**
 * 下载资源
 */
  mainWindow.webContents.session.on('will-download', (event, item, webContents) => {
    // 设置文件存放位置
    item.setSavePath(folderpath + `\\${item.getFilename()}`)
    item.on('updated', (event, state) => {
      if (state === 'interrupted') {
        console.log('Download is interrupted but can be resumed')
      } else if (state === 'progressing') {
        if (item.isPaused()) {
          console.log('Download is paused')
        } else {
          console.log(`Received bytes: ${item.getReceivedBytes()}`)
        }
      }
      console.log('update')
    })
    item.once('done', (event, state) => {
      if (state === 'completed') {
        console.log('Download successfully')
        webContents.send('download-success', item.getReceivedBytes())
      } else {
        console.log(`Download failed: ${state}`)
      }
    })
  })
}

// 主进程代码
ipcMain.on('download', (evt, args) => {
  const arr = args.split('+')
  downloadpath = arr[0]
  folderpath = arr[1]
  console.log(args)
  // evt.sender.send('tips',downloadpath);
  mainWindow.webContents.downloadURL(downloadpath)
})
app.on('ready', createWindow)

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (mainWindow === null) {
    createWindow()
  }
})

/**
 * Auto Updater
 *
 * Uncomment the following code below and install `electron-updater` to
 * support auto updating. Code Signing with a valid certificate is required.
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-electron-builder.html#auto-updating
 */

/*
import { autoUpdater } from 'electron-updater'

autoUpdater.on('update-downloaded', () => {
  autoUpdater.quitAndInstall()
})

app.on('ready', () => {
  if (process.env.NODE_ENV === 'production') autoUpdater.checkForUpdates()
})
 */
