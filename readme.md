# KIE Components for the FSI Credit Dispute demo app

## Projects

This is a collection of KIE projects with following relationship:

![Project Relationship Diagram](project-relationship.png "Project dependencies")

* Both the decisions project and case project have a direct Maven dependency on the model
* The Case project remotely invokes the decisions using a work item handler defined in the deployment descriptor using the maven coordinates of the decisions project
  * Can add a scanner as a fourth parameter expressed in ms
  * The FraudDispute process uses the new Decision Task
  * The decisions project includes the LocalDMNProcess that illustrates a local DMN invocation

## Domain Objects

* Cardholder
  * age:integer
  * balanceRatio: Float
  * incidentCount: integer
  * stateCode: String
  * status: String (GOLD|SILVER|STANDARD)
* FraudData
  * automated: boolean
  * customerStatus: String (GOLD|SILVER|STANDARD)
  * lineItemCount: Integer
  * maxDaysElapsed: Integer
  * totalFraudAmount: Float
* AdditionalInformation
  * questionId: Integer
  * questionPrompt: String
  * questionType: String (text,boolean,date,number)
  * answerValue: String

## Rest endpoints of interest

See the kie-test-util project for examples on invoking DMN decision through java api.

Includes a [Postman Collection](kie-test-util/fsi-credit-dispute.postman_collection.json) with all the relevant calls for the demo:

* Calling Additional Info rules for dynamic questions
* Invoking Automatic Processing decision
* Starting a Case (resulting in automatic or manual processing)

## Automatic Processing decision

![Credit dispute DRD](credit-dispute-drd.png "Simple example of multi-stage decision")

There are several steps in the decision:

* Calculating cardholder risk
  * ![Cardholder Risk Rating](cardholder-risk-rating.png "Multi-hit table with summation for cardholder risk")
* Calculating dispute risk
  * ![Dispute Risk Rating](dispute-risk-rating.png "Unique-hit table for dispute risk")
* Determining manual vs. automatic processing based on the risk scores
  * ![Process Automatically](process-automatically-determination.png "Simple FEEL expression")


## Additional Information Dynamic Questions

Input a cardholder and get a list of additional questions required.  Some examples:

![Technical rules](technical-rule-example.png "Example drools rule for generating an additional question")

![Status Rules Guided Table](guided-table-status.png "Set of rules for generating status related questions")

![Age Rules Guided Table](guided-table-age.png "Set of rules for generating age related questions")
