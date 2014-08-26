#MODAClouds Data Mapping tool

What is it?
-----------
See [deliverable 4.4.1](http://www.modaclouds.eu/wp-content/uploads/2013/10/MODAClouds_D4.4.1_MODACloudMLPackageOnDataPartitionAndReplicationProofOfConcept.pdf)

Quick start
-----------
$ cd target/

$ java -jar `dmc.jar` source=`PATH-to-the-xml-ER-model` target=`PATH-where-to-store-the-generated-model` type=`gdm-or-fdm-or-hdm` root=`root-node` name=`model-name`

Notes: 

* Folder xds contains the model reported in the deliverable.

* Folder xml contains some xml examples. 

* **root parameter must be indicated just in case of hdm (hierarchical) transformations**
