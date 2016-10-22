/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package yingchen.cs.musicplayer.visualizer;

// Data class to explicitly indicate that these bytes are raw audio data
public class AudioData
{
  public AudioData(byte[] bytes)
  {
    this.bytes = bytes;
  }

  public byte[] bytes;
}
