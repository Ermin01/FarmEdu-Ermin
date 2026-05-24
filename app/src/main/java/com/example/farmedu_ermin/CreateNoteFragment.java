package com.example.farmedu_ermin;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmedu_ermin.models.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteFragment extends Fragment {

    private EditText editTitle;
    private EditText editContent;

    private ImageView btnSave;
    private ImageView btnPin;
    private ImageView btnChecklist;
    private ImageView btnColor;
    private ImageView btnImage;
    private ImageView btnLock;
    private ImageView btnFormatList;
    private ImageView btnPdf;
    private ImageView btnBold;

    private TextView txtDate;

    private LinearLayout rootLayout;

    private boolean isPinned = false;
    private boolean isLocked = false;
    private boolean isChecklist = false;

    private String selectedColor = "#F7F4EE";

    private String imageUrl = "";

    private Uri selectedImageUri;

    private String noteId = "";
    private boolean isEditMode = false;

    // LOCK PIN

    private String pinCode = "";

    public CreateNoteFragment() {
    }

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {

                        if (uri != null) {

                            selectedImageUri = uri;

                            uploadImage();
                        }
                    });

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_create_note,
                container,
                false
        );

        rootLayout = view.findViewById(R.id.rootLayout);

        editTitle = view.findViewById(R.id.editTitle);

        editContent = view.findViewById(R.id.editContent);

        btnSave = view.findViewById(R.id.btnSave);

        btnPin = view.findViewById(R.id.btnPin);

        btnChecklist = view.findViewById(R.id.btnChecklist);

        btnColor = view.findViewById(R.id.btnColor);

        btnImage = view.findViewById(R.id.btnImage);

        btnLock = view.findViewById(R.id.btnLock);

        btnFormatList = view.findViewById(R.id.btnFormatList);

        btnPdf = view.findViewById(R.id.btnPdf);

        btnBold = view.findViewById(R.id.btnBold);

        txtDate = view.findViewById(R.id.txtDate);

        String currentDate =
                new SimpleDateFormat(
                        "dd MMM yyyy • HH:mm",
                        Locale.getDefault()
                ).format(new Date());

        txtDate.setText(currentDate);

        // =========================
        // LOAD EXISTING NOTE
        // =========================

        if (getArguments() != null) {

            noteId =
                    getArguments().getString(
                            "noteId",
                            ""
                    );

            isEditMode = true;

            editTitle.setText(
                    getArguments().getString(
                            "title",
                            ""
                    )
            );

            editContent.setText(
                    getArguments().getString(
                            "content",
                            ""
                    )
            );

            isPinned =
                    getArguments().getBoolean(
                            "pinned",
                            false
                    );

            isLocked =
                    getArguments().getBoolean(
                            "locked",
                            false
                    );

            pinCode =
                    getArguments().getString(
                            "pinCode",
                            ""
                    );

            selectedColor =
                    getArguments().getString(
                            "noteColor",
                            "#F7F4EE"
                    );

            rootLayout.setBackgroundColor(
                    Color.parseColor(selectedColor)
            );

            if (isPinned) {

                btnPin.setColorFilter(
                        Color.parseColor("#D4A017")
                );
            }

            if (isLocked) {

                btnLock.setColorFilter(Color.RED);
            }
        }

        // =========================
        // SAVE
        // =========================

        btnSave.setOnClickListener(v -> {

            saveNote();
        });

        // =========================
        // PIN
        // =========================

        btnPin.setOnClickListener(v -> {

            isPinned = !isPinned;

            if (isPinned) {

                btnPin.setColorFilter(
                        Color.parseColor("#D4A017")
                );

            } else {

                btnPin.setColorFilter(
                        Color.parseColor("#666666")
                );
            }
        });

        // =========================
        // LOCK
        // =========================

        btnLock.setOnClickListener(v -> {

            isLocked = !isLocked;

            if (isLocked) {

                pinCode = "1234";

                btnLock.setColorFilter(Color.RED);

                Toast.makeText(
                        getContext(),
                        "Note zaključan PIN-om 1234",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                pinCode = "";

                btnLock.setColorFilter(Color.BLACK);
            }
        });

        // =========================
        // CHECKLIST
        // =========================

        btnChecklist.setOnClickListener(v -> {

            isChecklist = true;

            editContent.append("\n☐ ");
        });

        // =========================
        // NUMBER LIST
        // =========================

        btnFormatList.setOnClickListener(v -> {

            editContent.append("\n1. ");
        });

        // =========================
        // BOLD
        // =========================

        btnBold.setOnClickListener(v -> {

            editContent.append(" **bold** ");
        });

        // =========================
        // IMAGE
        // =========================

        btnImage.setOnClickListener(v -> {

            imagePicker.launch("image/*");
        });

        // =========================
        // PDF
        // =========================

        btnPdf.setOnClickListener(v -> {

            exportPdf();
        });

        // =========================
        // COLORS
        // =========================

        btnColor.setOnClickListener(v -> {

            PopupMenu popupMenu =
                    new PopupMenu(getContext(), btnColor);

            popupMenu.getMenu().add("Cream");
            popupMenu.getMenu().add("Yellow");
            popupMenu.getMenu().add("Green");
            popupMenu.getMenu().add("Blue");
            popupMenu.getMenu().add("Pink");

            popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getTitle().toString()) {

                    case "Cream":
                        selectedColor = "#F7F4EE";
                        break;

                    case "Yellow":
                        selectedColor = "#FFF8D9";
                        break;

                    case "Green":
                        selectedColor = "#DFFFE2";
                        break;

                    case "Blue":
                        selectedColor = "#DDEBFF";
                        break;

                    case "Pink":
                        selectedColor = "#FFE1E1";
                        break;
                }

                rootLayout.setBackgroundColor(
                        Color.parseColor(selectedColor)
                );

                return true;
            });

            popupMenu.show();
        });

        return view;
    }

    // ======================================
    // SAVE NOTE
    // ======================================

    private void saveNote() {

        FirebaseUser user =
                FirebaseAuth.getInstance()
                        .getCurrentUser();

        if (user == null) {

            Toast.makeText(
                    getContext(),
                    "User nije prijavljen",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String uid = user.getUid();

        String title =
                editTitle.getText()
                        .toString()
                        .trim();

        String content =
                editContent.getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(title)
                && TextUtils.isEmpty(content)) {

            Toast.makeText(
                    getContext(),
                    "Unesi sadržaj",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // =========================================
        // CREATE NEW NOTE ONLY IF NOT EDIT
        // =========================================

        if (!isEditMode) {

            noteId =
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .collection("notes")
                            .document()
                            .getId();
        }

        // =========================================
        // SAFETY FIXES
        // =========================================

        if (selectedColor == null
                || selectedColor.isEmpty()) {

            selectedColor = "#F7F4EE";
        }

        if (imageUrl == null) {

            imageUrl = "";
        }

        if (pinCode == null) {

            pinCode = "";
        }

        // =========================================
        // CREATE NOTE MODEL
        // =========================================

        NoteModel note =
                new NoteModel(
                        noteId,
                        title,
                        content,
                        System.currentTimeMillis(),
                        isPinned,
                        isLocked,
                        isChecklist,
                        pinCode,
                        selectedColor,
                        imageUrl,
                        "",
                        "All Notes"
                );

        // =========================================
        // SAVE FIRESTORE
        // =========================================

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("notes")
                .document(noteId)
                .set(note)

                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            getContext(),
                            "Bilješka sačuvana",
                            Toast.LENGTH_SHORT
                    ).show();

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            getContext(),
                            "Greška: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                    e.printStackTrace();
                });
    }

    // ======================================
    // IMAGE UPLOAD
    // ======================================

    private void uploadImage() {

        if (selectedImageUri == null) return;

        StorageReference reference =
                FirebaseStorage.getInstance()
                        .getReference()
                        .child("note_images")
                        .child(System.currentTimeMillis() + "");

        reference.putFile(selectedImageUri)

                .addOnSuccessListener(taskSnapshot -> {

                    reference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                imageUrl = uri.toString();

                                Toast.makeText(
                                        getContext(),
                                        "Slika uploadovana",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            getContext(),
                            "Upload error",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    // ======================================
    // EXPORT PDF
    // ======================================

    private void exportPdf() {

        try {

            String title =
                    editTitle.getText()
                            .toString()
                            .trim();

            String content =
                    editContent.getText()
                            .toString()
                            .trim();

            if (title.isEmpty()) {

                title = "Note";
            }

            File folder =
                    new File(
                            requireContext()
                                    .getExternalFilesDir(null),
                            "FarmNotes"
                    );

            if (!folder.exists()) {

                folder.mkdirs();
            }

            File file =
                    new File(folder, title + ".pdf");

            PdfWriter writer =
                    new PdfWriter(file);

            PdfDocument pdfDocument =
                    new PdfDocument(writer);

            Document document =
                    new Document(pdfDocument);

            document.add(
                    new Paragraph(title)
                            .setBold()
                            .setFontSize(24f)
            );

            document.add(
                    new Paragraph("\n")
            );

            document.add(
                    new Paragraph(content)
                            .setFontSize(16f)
            );

            document.close();

            Toast.makeText(
                    getContext(),
                    "PDF sačuvan",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    getContext(),
                    "Greška PDF",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}