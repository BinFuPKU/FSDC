# FSDC: A Feature Selection Framework Based on Supervised  Data Clustering

paper: 
Liu H , Fu B , Jiang Z , et al. A feature selection framework based on supervised data clustering[C]// IEEE International Conference on Cognitive Informatics & Cognitive Computing. IEEE, 2017.

以往的特征选择主要考虑一维特征和标签之间的关系g(f,y)，高维特征和标签之间的高阶关系g(F,y)可以通过聚类来建模来实现，及g(F,y) -> g(cluster(F),y)，用聚类处理高维特征映射到聚类划分上。

Abstract:
Feature selection is an important step for data mining and machine learning to deal with the curse of dimensionality. In this paper, we propose a novel feature selection framework based on supervised data clustering. Instead of assuming there only exists low-order dependencies between features and the target variable, the proposed method directly estimates the high-dimensional mutual information between a candidate feature subset and the target variable through supervised data clustering. In addition, it can automatically determine the number of features to be selected instead of manually setting it in a prior. Experimental results show that the proposed method performs similar or better compared with state-of-the-art feature selection methods.

**Running Requirements:**
* jdk 1.7+
* weka.jar
* jxl.jar

libs can be downloaded freely from internet.
The source code is a demo used for academic exchange!


Bibtex:
```
@inproceedings{DBLP:conf/IEEEicci/LiuFJWH16,
  author    = {Hongzhi Liu and
               Bin Fu and
               Zhengshen Jiang and
               Zhonghai Wu and
               D. Frank Hsu},
  title     = {A feature selection framework based on supervised data clustering},
  booktitle = {15th {IEEE} International Conference on Cognitive Informatics {\&}
               Cognitive Computing ,ICCI*CC 2016, Palo Alto, CA, USA, August 22-23,
               2016},
  pages     = {316--321},
  year      = {2016},
  crossref  = {DBLP:conf/IEEEicci/2016},
  url       = {https://doi.org/10.1109/ICCI-CC.2016.7862054},
  doi       = {10.1109/ICCI-CC.2016.7862054},
  timestamp = {Mon, 22 May 2017 17:11:10 +0200},
  biburl    = {https://dblp.org/rec/bib/conf/IEEEicci/LiuFJWH16},
  bibsource = {dblp computer science bibliography, https://dblp.org}
}
```
