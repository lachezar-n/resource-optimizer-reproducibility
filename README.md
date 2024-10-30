# Reproducibility for SystemDS Resource Optimizer
This repository contains reproducibility resources for experiments on the SystemDS extension for resource optimization

The raw structure of the repository is:

```
resource-optimizer-reproducibility/
├── configs/ # archive for auto-generated scripts used at experiments
├── launch_scripts/ # scripts for automate the steps for experiments
├── python_utils/ # Python scripts for extracing data and generating diagrams
├── systemds/ # submodule pointing to the lachezar-n/systemds Repo
├── tests/ # SystemDS specific files for experiments
│   ├── esternal/ # extensive validaiton tests
│   └── resources/ # resource for experiments
│       └── data/ # metadata files for local experiments
│       └── scripts/ # DML scripts for local anf remote experments
```