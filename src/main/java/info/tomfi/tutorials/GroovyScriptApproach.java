package info.tomfi.tutorials;

import java.io.File;
import java.io.IOException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public final class GroovyScriptApproach implements ApproachInterface{
  public String getMessage(final String name, final int queueNum) {
    try {
      var shell = new GroovyShell();
      var scriptFile = new File(shell.getClassLoader().getResource("scripts/create_message.groovy").getFile());

      var script = shell.parse(scriptFile);

      var binding = new Binding();
      binding.setProperty("bindName", name);
      binding.setProperty("bindQueueNum", queueNum);

      script.setBinding(binding);

      return script.run().toString();
    } catch (IOException exc) {
      return exc.getMessage();
    }
  }
}
