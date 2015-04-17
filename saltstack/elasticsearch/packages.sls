/etc/yum.repos.d/elasticsearch.repo:
  file.managed:
    - user: root
    - group: root
    - source: salt://elasticsearch/files/elasticsearch.repo
    - template: jinja

elasticsearch:
  pkg.installed:
    - refresh: True