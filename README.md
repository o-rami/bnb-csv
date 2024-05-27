<h1 align="center">B&B</h1>

## Pacing 101
### Day 1:
* Create a schedule of tasks
* Readme/diagrams
* Present tasks and design docs for approval before writing code
* `Data -> domain -> ui`
  * [x] Flesh out base structure
  * [x] Solidify Models' fields and methods (est. 2 hours)
* Review Temporal Date material
### Day 2:
* Start the data layer:
  * [x] Handle exceptions `Data Exception class`
  * [x] Repository interfaces
  * [x] **Cr**eate (w/ testing, est. 1 hour)
    * [x] Test
  * [x] **U**pdate(w/ testing, est. 1 hour)
    * [x] Test
  * [x] **D**elete methods (w/ testing, est. 1 hour)
    * [x] Test
  * [x] Rough out required findBy methods
    * [x] Test
  * Provide additional if improves user exp
    * Don't want user to have to know/type in Host ID
    * Could provide last name instead
      * [x] implement `findByLastName()`
### Day 3:
* Progress presentation (10-15 minutes)
* Start domain layer
  * [x] Result
    * [x] Consider generic/templated type
      * Will make future stretch goals seamless
  * [x] Service
    * [x] VALIDATION VALIDATION VALIDATION
    * [x] Create repository doubles for testing
      * [x] GuestService
        * [x] Test
      * [x] HostService
      * [x] ReservationService
### Day 4:
* Start UI Layer
  * [x] Controller / VIew
  * [x] Step through the program and work on each CRUD feature one by one
    * [x] View existing reservations for a host
    * [x] Create a reservation for a guest with a host.
    * [x] Edit existing reservations.
    * [x] Cancel a future reservation.
### Day 5:
* Wrap up remaining tests, create test doubles
* Helper functions? Refactoring~
* Hunting bugs and fixing them (write targeted tests for bugs to prevent regression)
* Weekend: 
  * STRETCH GOALS
  * Regular Expression by RFC 5322 for Email Validation
  * Hunting bugs and fixing them
  * Polish
# High-Level Requirements
* [x] The administrator may view existing reservations for a host.
* [x] The administrator may create a reservation for a guest with a host.
* [x] The administrator may edit existing reservations.
* [x] The administrator may cancel a future reservation.
* [x] Spring Dependency Injection (Annotations)
# Data
### DataException
### HostRepository/FileRepository
* [x] Fields: filePath, header
* [x] FindById `client side??`, lastName... etc
### GuestRepository/FileRepository
* [x] Fields: filePath, header
* [x] FindById/FindByEmail/FindBynumber/FindBystate .. **Remember YAGNI**
### Reservations/directory/HostFiles
* [x] Fields: directoryPath, header
* [x] CRUD/serialize/deserialize/overwriteAll
# Domain
### Service
* [x] Mirror CRUD methods from repository to respective services
* [x] Create unique validation method
  * [x] Helper functions:
    * Nulls
    * Fields
    * Overlapping reservation dates
* [x] Create error messages for each expected error
# Models
* [x] Hosts
  * [x] Fields: 
    * id `GUID`, last_name `String`, email `String`, phone `String`, address `String`, city `String`, 
    * state `String`, postal_code `String`, standard_rate `BigDecimal`, weekend_rate `BigDecimal`
  * [x] Getters/setters/default constructor
  * [x] toString/hashcode override

* [x] Guests
  * [x] Fields: guest_id `int`, first_name `String`, last_name `String`, email `String`, phone `String`, state (also `String`...)
  * [x] Getters, setters, default constr
  * [x] toString/hashcode override

* [x] Reservations
  * [x] Stored across many files, one per host (named with the host GUID)
  * [x] id (int),start_date (LocalDate), end_date (LocalDate), guest_id (int), total (BigDecimal)
  * [x] Validation: 
    * [x] HostID as file name, must have start/end date, must have guest_id attached
    * [x] Guest/Host must already exist
    * [x] (startDate).isBefore(endDate) == True	MUST
    * [x] Reservations may never overlap
      * [x] Review Temporal Dates
* Notes:
  * Any need for enum?
    * Validation for email? must contain @ and .
    * postal code string size = 5
    * state string size = 2
  * Only CRUD for Reservations
# UI
### Main Menu
### `0 Exit:`
  * [x] Are you sure you want to exit?
  * [x] Display message (View.exit)
### `1 View Reservations for Host:`
  * [x] Get Host
    * [x] Display hosts `(view.displayHosts)`
    * [x] Prompt for input `(view.readInt)`
      * [x] Display reservations for selected host `(view.displayReservations(Host h))`
### `2 Create Reservation:`
  * [x] Select **Host**
  * [x] Select **start**/**end** date
  * [x] Select **Guest**
  * [x] Calculate **total** cost `start.until(end)` -> `#OfDays`? * `rate`
    * if friday/saturday `-> weekend rate`
    * else `-> standard rate`
  * [x] Display **Summary**
### `3 Update Reservation:`
  * Find reservation, edit start/ end date
    * [x] Display Hosts, Select **Host**
    * [x] Display Reservations, Select **Reservation**
      * [x] Which date would you like to edit?
        * Choose start/end/both
        * Recalculate total
        * Display summary
    * [x] Ask for confirmation
      * Cancel current change registration if not `y/Y`
### `4 Delete Reservation:`
  * [x] Find reservation, no current or past reservations should be displayed
  * [x] remove reservation, display message `[success/error]`
    * [x] Should not be able to cancel reservations that have already started
