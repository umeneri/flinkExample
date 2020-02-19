FROM docker.elastic.co/elasticsearch/elasticsearch:5.6.7
RUN elasticsearch-plugin remove x-pack
RUN bin/elasticsearch-plugin install analysis-kuromoji
RUN bin/elasticsearch-plugin install -b repository-s3
