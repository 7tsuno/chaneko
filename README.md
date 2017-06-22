# SlackOperator

Slackでの対話式会話botシステムです。

## 前提

* AWS Lambda
* Dynamo DB
* java 8以降

## 対話式システム

オペレータとの対話はステージと対応内容によって構成されます。  
ステージは、会話状況の状態を表します。
