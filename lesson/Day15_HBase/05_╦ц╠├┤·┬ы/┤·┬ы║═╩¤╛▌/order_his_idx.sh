#!/bin/sh
. /etc/profile


cd /opt/beifeng/hbase_pro
sqoop --options-file ./order_his_idx.opt


 