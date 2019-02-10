# vcftocsv
Makeshift Spring Boot app to convert VCF exports from OSX's Contacts app to CSV.

## Rationale
Because OSX's Contacts app search is really basic and I didn't want to use tools that require the upload of my complete list of contacts to the Internet, I created this.

## Features
* Supports only a limited of fields for now:
  * First name.
  * Last name.
  * Display as company.
  * Any number of emails per contact. Exported to CSV as `emails[$index].something`.
  * Any number of telephone numbers per contact. Exported to CSV as `telephones[$index].something`.
  * Any number of organisations per contact. Exported to CSV as `organisations[$index].something`.
  * Any number of addresses per contact. Exported to CSV as `addresses[$index].something`.

## How to Use
1. Build the application as you normally would and run it. It requires only the path to the VCF file to run.
1. Optionally, the CSV can be output to a file using the `-o` command line argument. By default, the CSV is output to stdout.
__