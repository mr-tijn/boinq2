#!/bin/bash
DBNAME=$1
echo "Exporting datasource $DBNAME"
cat export_datasource.sql | sed "s/DBNAME/$DBNAME/g" | mysql --batch -uroot -pweurtel -h 127.0.0.1 boinq >> datasource.tsv
cat export_track.sql | sed "s/DBNAME/$DBNAME/g" | mysql --batch -uroot -pweurtel -h127.0.0.1 boinq >> track.tsv
cat export_graphtemplate.sql | sed "s/DBNAME/$DBNAME/g" | mysql --batch -uroot -pweurtel -h127.0.0.1 boinq >> graphtemplate.tsv
cat export_edgetemplate.sql | sed "s/DBNAME/$DBNAME/g" | mysql --batch -uroot -pweurtel -h127.0.0.1 boinq >> edgetemplate.tsv
cat export_nodetemplate.sql | sed "s/DBNAME/$DBNAME/g" | mysql --batch -uroot -pweurtel -h127.0.0.1 boinq >> nodetemplate.tsv
