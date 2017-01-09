# Fredhopper/SAP Hybris Connector

With the Fredhopper *Connector for SAP Hybris*, you can leverage the advanced search, navigation, and merchandising capabilities of Fredhopper in your hybris-based online marketplace.

* [Overview](#overview)
	* [File Exporter](#file-explorer)
	* [Query Engine](#query-engine)
* [Installation and Configuration]()
	1. [Install the Plugin]()
	1. [Configure the Export Logic]()
	1. [Configure the Query Logic]()
* [Generating and Publishing Data]()
* [Front-End Integration]()
* [Backoffice Management]()

## Overview

The hybris Connector solution consists of two main conceptual components:
* **File exporter:** responsible for generating and exporting the CSV files to Fredhopper.
* **Query engine:** responsible for querying the Fredhopper online index.

### File Exporter

The exporter acts as a converter between the hybris data model and the Fredhopper input data model. After the conversion is complete, the exporter uploads the resulting `zip` file to the Fredhopper cloud indexing service and triggers an index update.

This process consists of the following stages:

1. Gathering of the relevant service layer data models in hybris.
1. Conversion of the service layer data model objects into Fredhopper DTOs.
1. Translation to and generation of CSV files out of the populated DTOs.
1. Upload of a zip file containing these CSV files to the Fredhopper cloud.
1. Triggering an index update.

The following diagram illustrates the concept:

[[images/Exporter_process.png]]

### Query Engine

Integrating the querying process into hybris consists of the following steps:

1. Querying the Fredhopper engine.
1. Parsing the response.
1. Transforming the Fredhopper response into the Solr response objects that came with hybris.

## Installation and Configuration

### 1. Install the Plugin

### 2. Configure the Export Logic

### 3. Configure the Query Logic

## Generating and Publishing Data

## Front-End Integration

## Backoffice Management