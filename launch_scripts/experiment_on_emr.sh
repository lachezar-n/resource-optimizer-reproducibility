#!/bin/bash

set -euo pipefail

start_init_ts=$(date +%s)
echo "Start init time: $(date +'%Y-%m-%d %H:%M:%S') ($start_init_ts)";
../systemds/scripts/resource/launch/cluster_launch.sh
finish_init_ts=$(date +%s)
echo "Finish init time: $(date +'%Y-%m-%d %H:%M:%S') ($finish_init_ts)"
echo "Total cluster initialization time: $((finish_init_ts - start_init_ts))"
