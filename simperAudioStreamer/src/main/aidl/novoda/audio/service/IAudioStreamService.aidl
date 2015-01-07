package novoda.audio.service;

interface IAudioStreamService
{
	void play(String url);
	String getFileName();
	long getCurrPlayingPosition();
	void stop();
	int getState();
}