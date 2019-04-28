package slasha.lanmu.business.profile.edit;

import slasha.lanmu.entity.card.UserCard;

class EditProfileImpl implements EditProfileContract.Presenter {

    private EditProfileContract.View mView;

    EditProfileImpl(EditProfileContract.View view) {
        mView = view;
    }

    @Override
    public void performSubmitUserInfo(UserCard userCard) {

    }
}
