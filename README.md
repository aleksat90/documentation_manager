# Introduction
Document collector is made for document organization. All date are saved on RDS Postgres on AWS exept files itself for the sake of speed, they are saved per request on local shared server. It can be replaced with E3 service as well. Idea of the app is to be installed on any computer and ultiemtly will be connected to the same DB.

# Main window
At main window all documents are listed and categorized. Documents can be filtered per document type, language and date of creation. There is a search bar as well which shows results on typing. Every column can be sorted.

![image](https://user-images.githubusercontent.com/22866358/69993571-60907880-154c-11ea-93d1-2740b086747c.png)

# New document
Document number is created automatically. First two letters are document type then two digit current year and at the end 3 digit incremental part.

![image](https://user-images.githubusercontent.com/22866358/69993592-6ab27700-154c-11ea-8e50-83f2b0840d4d.png)

# New revision
To create new revision, it is necessery to select document to which new revision will be added and browse for the document.

![image](https://user-images.githubusercontent.com/22866358/69993602-70a85800-154c-11ea-9d4c-77850f5f12aa.png)

Note: due to privacy of Amazon RDS, these lines are deleted.
