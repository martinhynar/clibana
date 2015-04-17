{% set version = '4.0.2' %}

Kibana:
  archive.extracted:
    - name: /opt/
    - source: https://download.elasticsearch.org/kibana/kibana/kibana-{{version}}-linux-x64.tar.gz
    - source_hash: https://download.elastic.co/kibana/kibana/kibana-{{version}}-linux-x64.tar.gz.sha1.txt
    - user: vagrant
    - group: vagrant
    - mode: 755
    - archive_format: tar
    - if_missing: /opt/kibana-{{version}}-linux-x64.tar.gz

link to kibana:
  file.symlink:
    - name: /opt/kibana
    - target: /opt/kibana-{{version}}-linux-x64/