# Amazon Glacier plugin

The Amazon Glacier Plugin allow you to interact with the Amazon Glacier archiving service. For detailed informations 
about Amazon Glacier please read the official documentation : http://docs.aws.amazon.com/amazonglacier/latest/dev/.

# Available commands

## `--describe-vault`

### Display the help

`archiver --glacier --describe-vault` or `archiver --glacier --describe-vault --help`

```
usage: archiver --glacier --describe-vault
    --a-store-id <ARCHIVER_STORE_ID>   Identifier of an archiver store.
    --help                             Print this message
```

### Describe a vault using an archiver store

`archiver --glacier  --describe-vault --a-store-id=my-vault`

```
  {
    "LastInventoryDate": "2014-04-08T01:34:56.462Z",
    "NumberOfArchives": 2,
    "VaultARN": "arn:aws:glacier:us-east-1:922146987616:vaults/my-vault",
    "CreationDate": "2014-04-07T15:59:48.715Z",
    "VaultName": "my-vault",
    "SizeInBytes": 65786
  }
```

### Describe a vault by providing a vault name

TODO

## `--get-job-output`

### Display the help

`archiver --glacier --get-job-output` or `archiver --glacier --get-job-output --help`

```
```

### Print a job output using an archiver store

`archiver --glacier  --get-job-output --a-store-id=glacier-archiver-vault --job-id=63ZEMSWIr2-4VyHUoJ3vQiBupP_tvoWAs-M-pMqyy52_9sehtSictM_h5GKPRGVorZsxbB4giFffFjf_yB99vz7NQV-G`

```
{
  "VaultARN": "arn:aws:glacier:us-east-1:922196456965:vaults/my-vault",
  "InventoryDate": "2014-04-08T01:34:56Z",
  "ArchiveList": [
    {
      "ArchiveId": "R_9yqIxFEruUeyWb4XnkPN0piefG477nr726WlE2wXFtFDJ1TM1gAKbP-rU-kVGuaArSLfSIKRiHH41vnNOMCYmazvts04b1YgbvQ37sODPop9HK5xAIhYTdqMebuZr626D2Pw9uOw",
      "SHA256TreeHash": "9a1ba5f11d979428bfcbe8d0159227ad03332fca4b234ce116c48af8d9efe9e6",
      "CreationDate": "2014-04-07T17:45:45Z",
      "ArchiveDescription": "my-archive",
      "Size": 125
    },
    {
      "ArchiveId": "JjBVy6zT-MHvnAnPNBHBkRAIXf8EL-CR1RDA5R6Estq_rg08h7EvZLbKcO8uHQ3tSYLxwV7aiakGXGe3iD8cTNdNXiDlwcQ5aACXVR8csdnMDPhOHrvDMhum9OObtZ8rAwgHHNt93g",
      "SHA256TreeHash": "889a93becb6fbfdf3b84a0ff288bc6a88f26090bbbb35ca0f4b2ffd0cf5e024b",
      "CreationDate": "2014-04-07T17:47:29Z",
      "ArchiveDescription": "my-archive",
      "Size": 125
    }
  ]
}

```

## `--help`

`archiver --glacier` or `archiver --glacier --help`

```
usage: archiver --glacier
    --backup
    --describe-vault                   Print informations about a vault.
    --download                         Download an archive.
    --get-job-output                   Gets the output of a job.
    --help                             Print this message.
    --initiate-archive-retrieval-job   Initiate an archive retrieval job.
    --initiate-vault-inventory-job     Initiate a vault inventory job.
    --list-jobs                        List jobs for a vault including jobs that are in-progress and jobs that have
                                       recently finished.
    --list-vaults                      List vaults associated to an AWS account.
    --upload-archive                   Upload an archive into a vault.
```

## --list-jobs 

### Display the help

`archiver --glacier --list-jobs` or `archiver --glacier --list-jobs --help`

```
usage: archiver --glacier --list-jobs

List jobs for a vault including jobs that are in-progress and jobs that have recently finished.
    --a-store-id <ARCHIVER_STORE_ID>   Identifier of an archiver store.
    --help                             Print this message
```

### List jobs using an archiver store

`archiver --glacier  --list-jobs --a-store-id=glacier-archiver-vault` 

```
{
  "JobList": [{
    "StatusCode": "InProgress",
    "ArchiveId": null,
    "ArchiveSHA256TreeHash": null,
    "CompletionDate": null,
    "Completed": false,
    "CreationDate": "2014-04-08T06:20:20.630Z",
    "JobDescription": null,
    "SNSTopic": null,
    "Action": "InventoryRetrieval",
    "ArchiveSizeInBytes": null,
    "VaultARN": "arn:aws:glacier:us-east-1:922146987616:vaults/my-vault",
    "JobId": "63ZEMSWIr2-4VyHUoJ3vQiBupP_tvoWAs-M-pMqyy52_9sehtSictM_h5GKPRGVorZsxbB4giFffFjf_yB99vz7NQV-G",
    "SHA256TreeHash": null,
    "InventorySizeInBytes": null,
    "RetrievalByteRange": null
  }],
  "Marker": null
}
```
