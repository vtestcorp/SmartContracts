@product
Feature: Functional tests for product

  @regression
  Scenario: TC001_Verify_creation_of_product
    Given I login as "superadmin" and create product
    When I login as "superadmin" and approve product
    Then Product should be created