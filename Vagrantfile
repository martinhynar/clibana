VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "chef/centos-6.6"

  config.vm.network :forwarded_port, guest: 9200, host: 9200
  config.vm.network :forwarded_port, guest: 9300, host: 9300
  config.vm.network :forwarded_port, guest: 5601, host: 5601

  config.vm.provider :virtualbox do |vb|
    vb.name = "clibana"
    vb.customize ["modifyvm", :id, "--memory", "1024"]
  end

  config.vm.synced_folder "./saltstack/", "/srv/salt/"

  config.vm.provision :salt do |salt|
    salt.minion_config = "./saltstack/minion"
    salt.run_highstate = true
    salt.colorize = true
  end
end
