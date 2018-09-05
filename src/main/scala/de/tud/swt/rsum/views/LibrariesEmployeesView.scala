package de.tud.swt.rsum.views

import de.tud.swt.rsum.IViewCompartment
import de.tud.swt.rsum.IViewRole
import de.tud.swt.rsum.usecase.mini.LibraryHasEmployees
import de.tud.swt.rsum.usecase.mini.Library
import de.tud.swt.rsum.usecase.mini.Employee
import scala.collection.mutable.ListBuffer
import de.tud.swt.rsum.RsumCompartment

class LibrariesEmployeesView extends IViewCompartment {
  
  protected def isViewable(classname: Object): Boolean = {
    if (classname.isInstanceOf[Library] || classname.isInstanceOf[Employee] || classname.isInstanceOf[LibraryHasEmployees])
      return true
    return false
  }

  protected def getNaturalRole(classname: Object): IViewRole = {
    var result: IViewRole = null
    if (classname.isInstanceOf[Library]) {
      result = new LibraryRole(null)
    } else if (classname.isInstanceOf[Employee]) {
      result = new EmployeeRole(null, null, 0.0)
    }
    return result
  }

  protected def getRelationalRole(classname: Object, sourceRole: IViewRole, targetRole: IViewRole): IViewRole = {
    var result: IViewRole = null
    if (classname.isInstanceOf[LibraryHasEmployees]) {
      result = new HasRole(sourceRole.asInstanceOf[LibraryRole], targetRole.asInstanceOf[EmployeeRole])
    }
    return result
  }  

  def getNewInstance(): IViewCompartment = new LibrariesEmployeesView()

  def getViewName(): String = "LibrariesEmployeesView"
  
  def createLibrary(name: String): LibraryRole = {
    return new LibraryRole(name)
  }

  def createEmployee(firstName: String, lastName: String, salary: Double): EmployeeRole = {
    return new EmployeeRole(firstName, lastName, salary)
  }

  def createHasEmployee(lib: LibrariesEmployeesView#LibraryRole, emp: LibrariesEmployeesView#EmployeeRole): HasRole = {
    //Rollen dürfen nicht doppelt sein
    roles.foreach { r =>
      if (r.isInstanceOf[HasRole]) {
        var relation: HasRole = r.asInstanceOf[HasRole]
        if (relation.getLibrary().equals(lib) && relation.getEmployee().equals(emp)) {
          return null
        }
      }
    }
    if (roles.contains(lib) && roles.contains(emp)) {
      return new HasRole(lib, emp)
    }
    return null
  }

  class LibraryRole(name: String) extends IViewRole {

    var hasRoles = ListBuffer[HasRole]()

    initialize()

    override def initialize(): Unit = {
      //create Library and bind roles  
      var lib: Library = new Library(name)
      initElement(lib, this)
    }

    def addEmployee(h: HasRole): Unit = {
      hasRoles = hasRoles :+ h
    }

    def getNameView(): String = {
      return +this getName ()
    }

    def setNameView(s: String): Unit = {
      +this setName (s)
      +this changeSomething ()
    }

    def deleteElement(): Unit = {
      +this deleteEverything ()
    }

    override def printViewRoles(): Unit = {
      LibrariesEmployeesView.this.printViewRoles()
    }

    override def toString(): String = {
      return getViewName() + " RLib: " + getNameView();
    }
    
    override def removeRole(): Unit = {
      LibrariesEmployeesView.this.removeRoleFromList(this)
    }
  }

  class EmployeeRole(firstName: String, lastName: String, salary: Double) extends IViewRole {

    var hasRoles = ListBuffer[HasRole]()

    initialize()

    override def initialize(): Unit = {
      var emp: Employee = new Employee(firstName, lastName, salary)
      initElement(emp, this)
    }

    def addLibrary(h: HasRole): Unit = {
      hasRoles = hasRoles :+ h
    }

    def getSalaryView(): Double = {
      return +this getSalary ()
    }

    def setSalaryView(s: Double): Unit = {
      +this setSalary (s)
      +this changeSomething ()
    }
    
    def getFirstNameView(): String = {
      return +this getFirstName ()
    }

    def setFirstNameView(s: String): Unit = {
      +this setFirstName (s)
      +this changeSomething ()
    }
    
    def getLastNameView(): String = {
      return +this getLastName ()
    }

    def setLastNameView(s: String): Unit = {
      +this setLastName (s)
      +this changeSomething ()
    }
    
    def deleteElement(): Unit = {
      +this deleteEverything()
    }
    
    override def printViewRoles(): Unit = {
      LibrariesEmployeesView.this.printViewRoles()
    }
    
    override def toString(): String = {
      return getViewName() + " REmp: " + getFirstNameView() + " " + getLastNameView();
    }
    
    override def removeRole(): Unit = {
      LibrariesEmployeesView.this.removeRoleFromList(this)
    }
  }

  class HasRole(library: LibrariesEmployeesView#LibraryRole, employee: LibrariesEmployeesView#EmployeeRole) extends IViewRole {

    initialize()

    override def initialize(): Unit = {

      //prüfen ob beide ein Natural haben
      var natLib: Library = library.player.right.get.asInstanceOf[Library]
      var natEmp: Employee = employee.player.right.get.asInstanceOf[Employee]

      //Starte Erstellung
      var libempRel: LibraryHasEmployees = new LibraryHasEmployees(natLib, natEmp)
      initElement(libempRel, this)
    }

    def getEmployee(): LibrariesEmployeesView#EmployeeRole = employee
    
    def getLibrary(): LibrariesEmployeesView#LibraryRole = library
    
    def deleteElement(): Unit = {
      +this deleteEverything()
    }
    
    override def printViewRoles(): Unit = {
      LibrariesEmployeesView.this.printViewRoles()
    }
    
    override def toString(): String = {
      return getViewName() + " RLibEmp";
    }
    
    override def removeRole(): Unit = {
      LibrariesEmployeesView.this.removeRoleFromList(this)
    }
  }

}