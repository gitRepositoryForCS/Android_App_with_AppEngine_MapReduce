/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package yingchen.cs.musicplayer.visualizer;

// Data class to explicitly indicate that these bytes are the FFT of audio data
public class FFTData
{
  public byte[] bytes;

  public FFTData(byte[] bytes)
  {
    this.bytes = bytes;
  }


}
