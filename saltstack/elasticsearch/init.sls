include:
  - elasticsearch.packages

/etc/elasticsearch/elasticsearch.yml
  file.managed:
    - user: root
    - group: root
    - source: salt://elasticsearch/files/elasticsearch.yml
    - template: jinja

elasticsearch running:
  service.running:
    - name: elasticsearch
    - enable: True

