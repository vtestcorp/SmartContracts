@taxcategory
Feature: Functional tests for tax category
​
  @wip1
  Scenario: TAX_CATEGORY_TC001_Verify_creation_of_tax_category
    Given I login as "adminmaker" and create tax category
    When I login as "adminchecker" and approve tax_category
   Then Tax category should be displayed at Admin queue