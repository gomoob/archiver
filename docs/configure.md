# Configure

When you use the `archiver` command you have 3 ways to provide configuration : 
 * Using the default Archiver configuration file located inside the `conf` directory
 * Using a custom configuration file with the `a-configuration-file`
 * Using command line options, when you use the command line options you can pull option values from you configuration 
   files or defined other option values directly at command line

## Common command options

Archiver is a command line tool which delegate its tasks to various archiving plugins. Each archiving plugin can define 
its own command line options or use archiver common command options. 

The archiver common command options are pre-defined options which allow to specify parameters associated to an 
archiver configuration file.

By convention all the archiver common command options begin with the `a-` prefix, for example to reference a store 
described inside an archiver configuration file the `--a-store-id` can be used. The `a-` prefix means "archiver", so the 
argument `--a-store-id` means "archiver store id". 

Multiple archiver common command options can be used, but all those options are not usable with all archiving 
plugins and commands. Only the options which are described in the command helps can be used, for example the Amazon 
Glacier `--describe-vault` command only allow the use of the `--a-store-id` common command option.

```
usage: archiver --glacier --describe-vault --help
    --a-store-id <ARCHIVER_STORE_ID>   Identifier of an archiver store.
    --help                             Print this message
```

### `--a-archive-id`

### `--a-configuration-file`

### `--a-credentials-id`

### `--a-store-id`


## Configuration file

To easier the use of the Archiver commands a configuration file can be provided. The configuration file allow to define 
3 type of elements : 
 * Credentials
 * Stores
 * Archives

Here is a sample configuration file. 

```
{
    "credentials" : [
        {
            "id" : "my-credentials",
            "key" : "XXXXXXXX",
            "secret" : "XXXXXXXX"
        }
    ],
    "stores" : [
        {
            "id" : "my-glacier-vault",
            "type" : "glacier",
            "credentials" : "my-credentials",
            "additionalConfiguration" : {
                "endpoint" : "https://glacier.us-east-1.amazonaws.com",
                "vaultName" : "vault-name"
            }
        }
    ],
    "archives" : [
        {
            "id" : "images",
            "cwd" : "/var/www/my-project/document-store/images",
            "dst" : "images.zip",
            "src" : ["**/*.png", "**/*.gif", "**/*.jpg"],
            "type" : "zip"
        },
        {
            "id" : "mysql-database",
            "dst" : "mysql-database.zip",
            "type" : "zip"
        }
    ]
}
```

### Credentials

### Stores

### Archives
