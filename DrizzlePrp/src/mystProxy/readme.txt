If you rename this package, you must change a String somewhere, due to reflection.
CustomSiteTemplate shows you how to make a Custom site handler.
  To redirect, call fileHandler.handle, passing in the new domain and target.(be careful not to set up an infinite loop).
  Do *not* use the request.getServerName(). Use the domain var that was passed along.
  To just read a file in your package, call fileHandler.handle3
  use fileHandler.getCookies rather than request.getCookies since request.getCookies can return null.