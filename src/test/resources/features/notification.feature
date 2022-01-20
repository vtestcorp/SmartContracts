@notification
Feature: Functional tests for notifications

  
  Scenario: TC008_Verify_trigger_of_notification
    Given Hooks and Templates are present in database
    And Pod logs are monitored
    When I login as "superadmin" and trigger notification
    Then Notifications should be triggered
