package reversi.model.network;

import java.io.IOException;

/** A network module for the reversi network function. */
public interface NetworkModule {

  /** Port of all modules. */
  int REVERSI_PORT = 43200;

  /**
   * Starts the network module.
   *
   * @throws IOException if exception occurs.
   */
  void start() throws IOException;

  void close() throws IOException;

  /** 
   * Returns true if module is running.
   * @return true if module is still running, false if it's not running. */
  boolean isRunning();
}
