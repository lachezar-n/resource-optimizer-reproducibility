#!/bin/bash

set -euo pipefail

start_init_ts=$(date +%s)
echo "Start time init: $(date +'%Y-%m-%d %H:%M:%S') ($start_init_ts)";
../systemds/scripts/resource/launch/single_node_launch.sh
finish_init_ts=$(date +%s)
echo "Finish time init: $(date +'%Y-%m-%d %H:%M:%S') ($finish_init_ts)"
#../systemds/scripts/resource/launch/single_node_run_script.sh
# requires enabled auto termination
finish_program_ts=$(date +%s)
echo "Finish time init: $(date +'%Y-%m-%d %H:%M:%S') ($finish_program_ts)"
