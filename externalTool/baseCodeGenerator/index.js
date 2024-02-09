function main() {
    // Check ./output/base directory exists. If exist, remove recursively.
    const fs = require('fs')
    if (fs.existsSync('./output/base')) {
        fs.rmSync('./output/base', { recursive: true })
    }
    
    // Copy all directory and files in ./template/base to ./output/base
    fs.cpSync('./template/base', './output/base', { recursive: true })


    // Replace all instances of 'hello' with 'world' in ./output/base
    const replace = require('replace-in-file')
    const settings = require('./input/settings.json')
    const options = {
        files: './output/base/src/main/resources/application.yml',
        from: [/\[\[SERVICE_INFO\.PACKAGE_NAME\]\]/g, /\[\[SERVICE_INFO\.SERVICE_NAME\]\]/g, 
            /\[\[SERVICE_INFO\.OPEN_PORT\]\]/g, /\[\[SERVICE_INFO\.HOST_PORT\]\]/g],
        to: [settings.SERVICE_INFO.PACKAGE_NAME, settings.SERVICE_INFO.SERVICE_NAME, 
            settings.SERVICE_INFO.OPEN_PORT, settings.SERVICE_INFO.HOST_PORT]
    }
    replace.sync(options)
}

main()