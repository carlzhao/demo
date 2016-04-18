#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <assert.h>
#include <sys/time.h>

int main()
{
	int keyboard;
	int ret, i;
	char c;

	fd_set readfd;
	struct timeval timeout;
	keyboard = open("/dev/tty", O_RDONLY | O_NONBLOCK);
	assert(keyboard > 0);

	while(1)
	{
		timeout.tv_sec = 5;
		timeout.tv_usec = 0;
		FD_ZERO(&readfd);
		FD_SET(keyboard, &readfd);

		ret = select(keyboard + 1, &readfd, NULL, NULL, &timeout);

		if(ret == -1)
			perror("select error !");

		else if (ret) 
		{
			if(FD_ISSET(keyboard, &readfd))
			{
				i = read(keyboard, &c, 1);

				if('\n' == c)
				{
					continue;
				}

				printf("input is %c\n", c);	

				if('q' == c)
				{
					break;
				}
			}
		}
		else if(ret == 0)
		{
			printf("timeout !\n");
		}

	}



}