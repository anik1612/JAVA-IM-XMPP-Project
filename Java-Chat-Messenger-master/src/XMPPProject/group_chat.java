package XMPPProject;

	public MultiUserChat createChatRoom(String roomName, String nickName, String password) throws Exception {
		MultiUserChat muc;
		try {
			EntityBareJid jid = JidCreate.entityBareFrom(roomName + "@conference." + connection.getServiceName());
			// MultiUserChat
			muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
			Resourcepart r = Resourcepart.from(nickName);
			
			MucCreateConfigFormHandle isCreated = muc.createOrJoin(r);
			if (true) {
				
				Form form = muc.getConfigurationForm();
				Form submitForm = form.createAnswerForm();
				
				List<FormField> fields = form.getFields();
				for (int i = 0; fields != null && i < fields.size(); i++) {
					if (FormField.Type.hidden != fields.get(i).getType() && fields.get(i).getVariable() != null) {
				
						submitForm.setDefaultAnswer(fields.get(i).getVariable());
					}
				}
				
				List owners = new ArrayList();
				owners.add(connection.getUser().asEntityBareJidString());
				submitForm.setAnswer("muc#roomconfig_roomowners", owners);
				
				submitForm.setAnswer("muc#roomconfig_persistentroom", true);
				
				submitForm.setAnswer("muc#roomconfig_membersonly", false);
				
				submitForm.setAnswer("muc#roomconfig_allowinvites", true);
				if (password != null && password.length() != 0) {
					
					submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
					
					submitForm.setAnswer("muc#roomconfig_roomsecret", password);
				}
				
				submitForm.setAnswer("muc#roomconfig_enablelogging", true);
				
				submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
				
				submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
				
				submitForm.setAnswer("x-muc#roomconfig_registration", false);
				
				muc.sendConfigurationForm(submitForm);
 
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}
 
	
	public MultiUserChat joinMultiUserChat(String roomName, String nickname, String password) throws Exception {
		EntityBareJid jid = JidCreate.entityBareFrom(roomName + "@conference." + connection.getServiceName());

		MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);

		Resourcepart r = Resourcepart.from(nickname);
		muc.join(r);
		return muc;
	}

	public void sendMessageMultiUserChat(String roomName, String msg) throws Exception {
		EntityBareJid jid = JidCreate.entityBareFrom(roomName + "@conference." + connection.getServiceName());

		MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
		muc.sendMessage(msg);
	}
 

	public MultiUserChat getMultiUserChat(String roomName) throws Exception {
		EntityBareJid jid = JidCreate.entityBareFrom(roomName + "@conference." + connection.getServiceName());
		MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(jid);
		return muc;
	}
 

	public void addMultiUserChatMessageListener(String roomName, MessageListener messageListener) throws Exception {
		MultiUserChat muc = getMultiUserChat(roomName);
		muc.addMessageListener(messageListener);
	}
}
