
//dev 内容
这里做git练习： 这是dev分支，我要做的操作是，
更改这个内容，然后提交分支。
1. 在master的基础之上新建分支
    git branch dev 或者使用idea的操作
2. git checkout dev 切回分支。

3. 修改内容，然后先提交到本地仓库，然后push到远程仓库。


//主分支内容
1.这里是主分支，需要合并dev分支的ignore，会有冲突，练习解决。

2.练习合并，需要合并dev分支，这两个分支冲突的地方在git.md文件，内容不一样，查看idea中
    如何合并你分支已经决绝冲突。
=======


# 新练习
idea的图形显示，对于远程仓库和本地仓库显示的形式是不一致，查看时怎么不一样的。
1.本地修改文件，提交到本地仓库，查看状态。  
    发现图示中，本地master分支的指针发生了改变，变成可最新，而远程的没有发生改变，
2.将本地的内容，提交到远程中。观察改变。
    提交完成后，发现origin&master也发生了改变。变成与本地一样了
    