/*

This example reads from the default PCM device
and writes to standard output for 5 seconds of data.

*/

/* Use the newer ALSA API */
#define ALSA_PCM_NEW_HW_PARAMS_API

#include <stdio.h>
#include <stdlib.h>
#include <alsa/asoundlib.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

// Global variables
int clientSocket, portNum, nBytes;
struct sockaddr_in serverAddr;
socklen_t addr_size;

int start_udpsocket();
void send_data(char*);

int size;

int main() {
  long loops;
  int rc;
  snd_pcm_t *handle;
  snd_pcm_hw_params_t *params;
  unsigned int val;
  int dir;
  snd_pcm_uframes_t frames;
  char *buffer;
  
  start_udpsocket();

  /* Open PCM device for recording (capture). */
  rc = snd_pcm_open(&handle, "plughw:1,1",
                    SND_PCM_STREAM_CAPTURE, 0);
  if (rc < 0) {
    fprintf(stderr,
            "unable to open pcm device: %s\n",
            snd_strerror(rc));
    exit(1);
  }

  /* Allocate a hardware parameters object. */
  snd_pcm_hw_params_alloca(&params);

  /* Fill it in with default values. */
  snd_pcm_hw_params_any(handle, params);

  /* Set the desired hardware parameters. */

  /* Interleaved mode */
  snd_pcm_hw_params_set_access(handle, params,
                      SND_PCM_ACCESS_RW_INTERLEAVED);

  /* Signed 16-bit little-endian format */
  snd_pcm_hw_params_set_format(handle, params,
                              SND_PCM_FORMAT_S16_LE);

  /* Two channels (stereo) */
  snd_pcm_hw_params_set_channels(handle, params, 2);

  /* 44100 bits/second sampling rate (CD quality) */
  val = 44100;
  snd_pcm_hw_params_set_rate_near(handle, params,
                                  &val, &dir);

  /* Set period size to 32 frames. */
  frames = 400;
  snd_pcm_hw_params_set_period_size_near(handle,
                              params, &frames, &dir);

  /* Write the parameters to the driver */
  rc = snd_pcm_hw_params(handle, params);
  if (rc < 0) {
    fprintf(stderr,
            "unable to set hw parameters: %s\n",
            snd_strerror(rc));
    exit(1);
  }

  /* Use a buffer large enough to hold one period */
  snd_pcm_hw_params_get_period_size(params,
                                      &frames, &dir);
  size = frames * 4; /* 2 bytes/sample, 2 channels */
  buffer = (char *) malloc(size);

  /* We want to loop for 5 seconds */
  snd_pcm_hw_params_get_period_time(params,
                                         &val, &dir);
  
  //fprintf(stdout, "\n value = %d %d %d \n", val, size, (int)frames);
  loops = 5000000 / val;

  while (1 > 0) {
    //loops--;
    rc = snd_pcm_readi(handle, buffer, frames);
    //if (rc == -EPIPE) {
      /* EPIPE means overrun */
    //  fprintf(stderr, "overrun occurred\n");
    //  snd_pcm_prepare(handle);
 // }
  
   sendto(clientSocket,buffer, size,0,(struct sockaddr *)&serverAddr,addr_size);

  
    //send_data(buffer);
    //rc = write(1, buffer, size);
    //if (rc != size)
      //fprintf(stderr, "short write: wrote %d bytes\n", rc);
  }

  snd_pcm_drain(handle);
  snd_pcm_close(handle);
  free(buffer);

  return 0;
}

void send_data(char* buffer){

//nBytes = strlen(buffer);    
/*Send message to server*/
sendto(clientSocket,buffer, size,0,(struct sockaddr *)&serverAddr,addr_size);

}



int start_udpsocket(){


  /*Create UDP socket*/
  clientSocket = socket(PF_INET, SOCK_DGRAM, 0);

  /*Configure settings in address struct*/
  serverAddr.sin_family = AF_INET;
  serverAddr.sin_port = htons(5001);
  serverAddr.sin_addr.s_addr = inet_addr("228.5.6.7");
  memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);  

  /*Initialize size variable to be used later on*/
  addr_size = sizeof serverAddr;

  return 0;
}
