# sample run



$ sbt run
other session:
$ nc -l 9000
{"name": "nc test"}


# terraformでEMR
[TerraformでAmazon EMR クラスターを構築 | Developers.IO](https://dev.classmethod.jp/articles/create-amazon-emr-cluster-with-terraform/)
→ 結構古かった

公式
[AWS: aws_emr_cluster - Terraform by HashiCorp](https://www.terraform.io/docs/providers/aws/r/emr_cluster.html#configurations-1)


# yarn
[あの日見たYARNのお仕事を僕達はまだ知らない。 - Qiita](https://qiita.com/keigodasu/items/09f7e0a15d721b0b5212)


# flinkのjar実行

emrでflinkを動かす場合、hadoopも入れないとクラスタ作成がtimeoutになった…。。

セキュリティグループでSSH追加し、application idを取る


# emrで動かすflink
[Amazon EMR 内で Flink ジョブを操作する - Amazon EMR](https://docs.aws.amazon.com/ja_jp/emr/latest/ReleaseGuide/flink-jobs.html)
実行方法

```
aws emr add-steps --cluster-id j-1846X1IEZL0J4 \
--steps Type=CUSTOM_JAR,Name=Flink_Submit_To_Long_Running,Jar=command-runner.jar,\
Args="flink","run","-m","yarn-cluster","-yid","application_1589104317608_0001","-yn","2",\
"/usr/lib/flink/examples/streaming/WordCount.jar",\
"--input","s3://flink-examples/test.txt","--output","s3://flink-examples/out/" \
--region ap-northeast-1
```

[S3 Management Console](https://s3.console.aws.amazon.com/s3/buckets/flink-examples/?region=ap-northeast-1&tab=overview)

- マスターノードの/usr/lib/flinkがflinkのプログラム
- コアノードでjob managerとtask managerが別containerで動く
- yarn applicationが1つ動作していないと動かない

