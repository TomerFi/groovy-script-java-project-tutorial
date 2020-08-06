package info.tomfi.tutorials;

public final class MethodApproach implements ApproachInterface{
  private static String createMessage(final String name, final int queueNum) {
    return String.format(
        "Hello %s, you're number %d, please wait patiently, here is some info:\n" +
        "Anim incididunt deserunt ex ad do aliquip.\n" +
        "Ullamco est Lorem nisi magna duis esse laboris commodo eu.\n" +
        "Anim laborum commodo sint est dolor veniam id non sint aliquip amet pariatur.\n" +
        "Ex non incididunt duis aliqua qui quis eiusmod ex eiusmod irure nisi sint anim.\n" +
        "Ipsum voluptate laboris eiusmod sint ea do.", name, queueNum);
  }

  public String getMessage(final String name, final int queueNum) {
    return createMessage(name, queueNum);
  }
}
