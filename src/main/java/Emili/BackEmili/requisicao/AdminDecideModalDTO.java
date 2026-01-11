package Emili.BackEmili.requisicao;

import jakarta.validation.constraints.NotNull;

public class AdminDecideModalDTO {
    @NotNull
    private Modal modal;

    public Modal getModal() {
        return modal;
    }

    public void setModal(Modal modal) {
        this.modal = modal;
    }
}
